package ca.sheridancollege.ozcelikh.database;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.ozcelikh.beans.Event;
import ca.sheridancollege.ozcelikh.beans.User;

/**
 * 
 * @author Hizir Ozcelik, November 2021
 */

@Repository
public class DatabaseAccess {

	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User findUserAccount(String email) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM sec_user WHERE email = :email";
		namedParameters.addValue("email", email);

		ArrayList<User> userList = (ArrayList<User>) jdbc.query(query, namedParameters,
				new BeanPropertyRowMapper<User>(User.class));

		if (userList.size() > 0)
			return userList.get(0);
		else
			return null;

	}

	public List<String> getRolesById(Long userId) {

		ArrayList<String> roleList = new ArrayList<String>();

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT user_role.userId, sec_role.roleName " + "FROM user_role, sec_role "
				+ "WHERE user_role.roleId = sec_role.roleId " + "AND userId = :userId";

		namedParameters.addValue("userId", userId);

		List<Map<String, Object>> rows = jdbc.queryForList(query, namedParameters);

		for (Map<String, Object> row : rows) {
			roleList.add((String) row.get("roleName"));
		}

		return roleList;

	}

	public void addUser(String name, String lastName, String email, String password) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "INSERT INTO sec_user" + "(name, lastName, email, encryptedPassword, enabled) "
				+ "VALUES (:name, :lastName, :email, :encryptedPassword, 1) ";

		namedParameters.addValue("name", name);
		namedParameters.addValue("lastName", lastName);
		namedParameters.addValue("email", email);
		namedParameters.addValue("encryptedPassword", passwordEncoder.encode(password));

		jdbc.update(query, namedParameters);

	}
	
	public void addRole(Long userId, Long roleId) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "INSERT INTO user_role (userId, roleId) " + "VALUES (:userId, :roleId)";
		namedParameters.addValue("userId", userId);
		namedParameters.addValue("roleId", roleId);

		jdbc.update(query, namedParameters);
	}

	public void addEvent(Long userId, Event event) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "INSERT INTO event" + "(userId, title, eventDate, eventTime, details, category, tag) "
				+ "VALUES (:userId, :title, :eventDate, :eventTime, :details, :category, :tag) ";

		namedParameters.addValue("userId", userId);
		namedParameters.addValue("title", event.getTitle());
		namedParameters.addValue("eventDate", event.getEventDate());
		namedParameters.addValue("eventTime", event.getEventTime());
		namedParameters.addValue("details", event.getDetails());
		namedParameters.addValue("category", event.getCategory());
		namedParameters.addValue("tag", event.getTag());

		jdbc.update(query, namedParameters);

	}

	

	public List<Event> getEventList(Long userId) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		String query = "SELECT * FROM event WHERE userId = :userId ORDER BY eventDate";
		namedParameters.addValue("userId", userId);
				
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Event>(Event.class));

	}

}
