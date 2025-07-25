DELETE FROM attachments WHERE id IN (1, 2, 3, 4);

DELETE FROM comments WHERE id IN (1, 2, 3, 4);

DELETE FROM users_roles WHERE user_id IN (1, 2, 3);

DELETE FROM users_projects WHERE user_id IN (1, 2, 3) OR project_id IN (1, 2, 3);

DELETE FROM labels_projects WHERE label_id IN (1, 2, 3, 4) OR project_id IN (1, 2, 3);

DELETE FROM tasks WHERE id IN (1, 2, 3, 4);

DELETE FROM labels WHERE id IN (1, 2, 3, 4);

DELETE FROM projects WHERE id IN (1, 2, 3);

DELETE FROM users WHERE id IN (1, 2, 3);

DELETE FROM roles WHERE id IN (1, 2, 3);
