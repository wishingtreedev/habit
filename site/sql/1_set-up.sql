CREATE TABLE IF NOT EXISTS activities
(
    id          INTEGER PRIMARY KEY,
    habit       TEXT NOT NULL,
    description TEXT NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_habit ON activities (habit);