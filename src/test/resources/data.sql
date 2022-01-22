INSERT INTO USERS (id, name, password, email, activated, first_name, last_name, age, gender, role) VALUES
(1, 'User test name 1', '$2a$12$cQ9YMUMzHpSL75ayf/wCBus0CL7n4Mb7.yA2efw1pHc9emsM8KEoG', 'user@test1.com', 1, 'Test first name1', 'Test last name1', 1, 1, 'ADMIN'),
(2, 'User test name 2', '$2a$12$au3cmOk05SGknNbjmdF0IebC8OhaF6n3Y623T918UnS1u5M792M9i', 'user@test2.com', 1, 'Test first name2', 'Test last name2', 2, 1, 'USER'),
(3, 'User test name 3', '$2a$12$F4IZuwr4TCBz1xxArfBR7uifdI.qyubvwIfdgxsg397XzVD9Mo0cO', 'user@test3.com', 1, 'Test first name3', 'Test last name3', 3, 1, 'USER'),
(4, 'User test name 4', '$2a$12$jQlKe1zX33fOeaJgp5C71eWDP3KxB1xWnSDzoLLZUsvX9lnBGu2nu', 'user@test4.com', 1, 'Test first name4', 'Test last name4', 4, 1, 'USER'),
(5, 'User test name 5', '$2a$12$9HhUiXlUXnK0VaWrgeH1rOb/PvDsvcRWzPP6pf4oRuhY.zXph46w6', 'user@test5.com', 1, 'Test first name5', 'Test last name5', 5, 1, 'USER'),
(6, 'User test name 6', '$2a$12$6rhwsR.GbzLT/y669aI3aeHZldbylBhmrSudgnzoRh1GL/aKkoPuq', 'user@test6.com', 0, 'Test first name6', 'Test last name6', 6, 1, 'USER'),
(7, 'User test name 7', '$2a$12$qgUEHaukTqcoVNNksPlzveTx2jRoQysF2u4/eKpTaYgMFL5WJN3S6', 'user@test7.com', 0, 'Test first name7', 'Test last name7', 7, 1, 'USER'),
(8, 'User test name 8', '$2a$12$mIcDvX13BuHqyR1i205BKuKY.6mO8PJ1muMJJXZASHJYQhyYNHEGy', 'user@test8.com', 0, 'Test first name8', 'Test last name8', 8, 1, 'USER'),
(9, 'Token test name 1', '$2a$12$cQ9YMUMzHpSL75ayf/wCBus0CL7n4Mb7.yA2efw1pHc9emsM8KEoG', 'token@test1.com', 1, 'Test first name1', 'Test last name1', 1, 1, 'USER'),
(10, 'Token test name 2', '$2a$12$au3cmOk05SGknNbjmdF0IebC8OhaF6n3Y623T918UnS1u5M792M9i', 'token@test2.com', 1, 'Test first name2', 'Test last name2', 2, 1, 'USER'),
(11, 'Token test name 3', '$2a$12$F4IZuwr4TCBz1xxArfBR7uifdI.qyubvwIfdgxsg397XzVD9Mo0cO', 'token@test3.com', 1, 'Test first name3', 'Test last name3', 3, 1, 'USER'),
(12, 'Token test name 4', '$2a$12$jQlKe1zX33fOeaJgp5C71eWDP3KxB1xWnSDzoLLZUsvX9lnBGu2nu', 'token@test4.com', 1, 'Test first name4', 'Test last name4', 4, 1, 'USER'),
(13, 'Token test name 5', '$2a$12$9HhUiXlUXnK0VaWrgeH1rOb/PvDsvcRWzPP6pf4oRuhY.zXph46w6', 'token@test5.com', 1, 'Test first name5', 'Test last name5', 5, 1, 'USER'),
(14, 'Token test name 6', '$2a$12$6rhwsR.GbzLT/y669aI3aeHZldbylBhmrSudgnzoRh1GL/aKkoPuq', 'token@test6.com', 0, 'Test first name6', 'Test last name6', 6, 1, 'USER'),
(15, 'Token test name 7', '$2a$12$qgUEHaukTqcoVNNksPlzveTx2jRoQysF2u4/eKpTaYgMFL5WJN3S6', 'token@test7.com', 0, 'Test first name7', 'Test last name7', 7, 1, 'USER'),
(16, 'Token test name 8', '$2a$12$mIcDvX13BuHqyR1i205BKuKY.6mO8PJ1muMJJXZASHJYQhyYNHEGy', 'token@test8.com', 1, 'Test first name8', 'Test last name8', 8, 1, 'USER'),
(17, 'Workout test name 1', '$2a$10$tCeixUEMo5NS3S6946nUGu/4yV7Zkf9cqHpDbjOBENidMTifhdhoS', 'workout@test1.com', 1, 'Test first name1', 'Test last name1', 1, 1, 'USER'),
(18, 'Workout test name 2', '$2a$12$EVld3BKMbJy60YJFfqYgQOoI6JpwWyEsNUOq2H4Z3ny5wEYW02SnW', 'workout@test2.com', 1, 'Test first name2', 'Test last name2', 2, 1, 'USER'),
(19, 'test', '$2a$12$QPV1B4XAm3SDYtdIydlQf.TSRz7NKQ1jtbEFAWQTdXhe2Xp.4mUji', 'test@email.com', 0, 'Test first name', 'Test last name', 8, 1, 'USER');

INSERT INTO VERIFICATION_TOKEN (id, expiry_date, token, user_id) VALUES
(1, TIMESTAMPADD(DAY, +1, CURRENT_DATE), 'Test token 1', 9),
(2, TIMESTAMPADD(DAY, +1, CURRENT_DATE), 'Test token 2', 12),
(3, TIMESTAMPADD(DAY, +1, CURRENT_DATE), 'Test token 3', 15),
(4, NOW(), 'Test token 4', 16);
