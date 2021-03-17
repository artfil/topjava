package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {
        ValidationUtil.jdbcValidation(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user.getRoles(), user.getId());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            deleteRoles(user.getId());
            insertRoles(user.getRoles(), user.getId());
        }
        return user;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return getRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return getRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        Map<Integer, Set<Role>> roles = getAllRoles();
        return jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER).stream()
                .peek(user -> user.setRoles(roles.get(user.getId())))
                .collect(Collectors.toList());
    }

    public User getRoles(User user) {
        if (user != null)
            user.setRoles(jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?",
                    (resultSet, i) -> Role.valueOf(resultSet.getString("role")), user.getId()));
        return user;
    }

    public Map<Integer, Set<Role>> getAllRoles() {
        return jdbcTemplate.query("SELECT * FROM user_roles", resultSet -> {
            Map<Integer, Set<Role>> map = new LinkedHashMap<>();
            while (resultSet.next()) {
                int id = Integer.parseInt(resultSet.getString("user_id"));
                map.putIfAbsent(id, new HashSet<>());
                map.get(id).add(Role.valueOf(resultSet.getString("role")));
            }
            return map;
        });
    }

    public void insertRoles(Set<Role> roles, int id) {
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", roles, roles.size(), (preparedStatement, role) -> {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, role.name());
        });
    }

    public void deleteRoles(int id) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
    }
}
