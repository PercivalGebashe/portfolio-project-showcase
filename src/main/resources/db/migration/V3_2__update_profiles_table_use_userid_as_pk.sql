-- =========================================
-- V2__update_profiles_table_use_userid_as_pk.sql
-- Update the profiles table to use user_id as both PK and FK
-- =========================================

-- 1️⃣ Drop the old primary key (profile_id)
ALTER TABLE profiles DROP CONSTRAINT profiles_pkey;

-- 2️⃣ Drop the profile_id column completely
ALTER TABLE profiles DROP COLUMN profile_id;

-- 3️⃣ Make user_id the new primary key
ALTER TABLE profiles ADD PRIMARY KEY (user_id);

-- 4️⃣ Ensure user_id remains a foreign key to user_accounts
ALTER TABLE profiles
    DROP CONSTRAINT fk_profile_user,
    ADD CONSTRAINT fk_profile_user FOREIGN KEY (user_id)
    REFERENCES user_accounts (user_id)
    ON DELETE CASCADE;

