INSERT INTO users (email, password, first_name, last_name, role, is_email_verified, created_at)
VALUES
  ('student1@example.com', '$2a$10$kzv7E9n9ZKx3E1XW7EvPeOQ0zQaDkzSvxZ.kGVQhEbXoRY.89i9nK', 'John', 'Doe', 'STUDENT', true, CURRENT_TIMESTAMP),
  ('parent1@example.com', '$2a$10$kzv7E9n9ZKx3E1XW7EvPeOQ0zQaDkzSvxZ.kGVQhEbXoRY.89i9nK', 'Jane', 'Smith', 'PARENT', true, CURRENT_TIMESTAMP);
