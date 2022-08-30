INSERT INTO sec_user (name, lastName, email, encryptedPassword, enabled)
VALUES ('Simon', 'Hood', 'simon.hood@sheridancollege.ca', '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', 1);

INSERT INTO event (userId, title, eventDate, eventTime, details, category, tag)
VALUES (1, 'Dentist', '2021-12-02', '12:00', 'Leave before 30 mins from appointment', 'Personal', 'Red');

INSERT INTO event (userId, title, eventDate, eventTime, details, category, tag)
VALUES (1, 'Class', '2021-12-12', '12:00', 'Dont forget to upload the lecture',  'Work', 'Blue');

INSERT INTO event (userId, title, eventDate, eventTime, details, category, tag)
VALUES (1, 'Call CRA', '2021-12-21', '09:00', 'Regarding 2021 tax report',  'Personal', 'Red');

INSERT INTO sec_role (roleName)
VALUES ('ROLE_USER');
 
INSERT INTO user_role (userId, roleId)
VALUES (1, 1);
