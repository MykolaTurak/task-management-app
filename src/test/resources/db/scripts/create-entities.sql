INSERT INTO users (id, user_name, password, email, first_name, last_name, is_deleted, temporary_token, telegram_chat_id) VALUES
(1, 'test_user', '$2a$10$RrhM5OKejkvKRHsB05Ee6eQZtxqs1eCVpT9gmoS6AFoCWzaKJHpsi', 'test@example.com', 'Test', 'User', 0, NULL, NULL),
(2, 'alice', 'alice_pass', 'alice@example.com', 'Alice', 'Wonder', 0, NULL, 1234567890),
(3, 'bob', 'bob_pass', 'bob@example.com', 'Bob', 'Builder', 0, 'temp_token_abc', NULL);

INSERT INTO projects (id, name, description, start_date, end_date, status, is_deleted) VALUES
(1, 'Test Project', 'Project used for testing INSERTs', '2025-07-01 10:00:00', '2025-08-01 18:00:00', 1, 0),
(2, 'New Website', 'Development of new company website', '2025-06-15 09:00:00', '2025-09-30 17:00:00', 2, 0),
(3, 'Mobile App', 'Mobile application for e-commerce', '2025-07-20 12:00:00', '2025-10-10 18:00:00', 1, 0);

INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id, is_deleted) VALUES
(1, 'Add file upload interface', 'Create frontend form and connect it to backend', 2, 1, '2025-08-01 18:00:00', 1, 1, 0),
(2, 'Design landing page', 'Create mockups and prototypes', 1, 1, '2025-07-15 12:00:00', 2, 2, 0),
(3, 'Implement payment gateway', 'Integrate Stripe API', 3, 0, '2025-08-20 18:00:00', 3, 3, 0),
(4, 'Fix bugs in registration', 'Resolve reported issues', 1, 2, '2025-07-10 15:00:00', 1, 2, 0);

INSERT INTO attachments (id, task_id, drop_box_file_id, filename, upload, is_deleted) VALUES
(1, 1, 'dbx123abc', 'design_mockup_1.png', '2025-07-20 10:00:00', 0),
(2, 1, 'dbx456def', 'specifications.pdf', '2025-07-20 10:05:00', 0),
(3, 2, 'dbx789ghi', 'landing_page_sketch.jpg', '2025-07-10 09:00:00', 0),
(4, 3, 'dbx101jkl', 'payment_flowchart.pdf', '2025-07-25 11:30:00', 0);

INSERT INTO comments (id, task_id, user_id, text, timestamp, is_deleted) VALUES
(1, 1, 1, 'Fixed issue #45 and #46.', '2025-07-21 14:00:00', 0),
(2, 2, 2, 'Mockups look good, ready for review.', '2025-07-11 16:30:00', 0),
(3, 3, 3, 'Stripe integration almost complete.', '2025-07-26 10:15:00', 0),
(4, 4, 2, 'Fixed issue #45 and #46.', '2025-07-09 18:00:00', 0);

INSERT INTO labels (id, name, color, is_deleted) VALUES
(1, 'Frontend', '#ff5733', 0),
(2, 'Backend', '#33c4ff', 0),
(3, 'Bug', '#ff3333', 0),
(4, 'Feature', '#33ff77', 0);

INSERT INTO users_roles (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 2);

INSERT INTO users_projects (user_id, project_id) VALUES
(1, 1),
(2, 1),
(2, 2),
(3, 3);

INSERT INTO labels_projects (label_id, project_id) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 3);
