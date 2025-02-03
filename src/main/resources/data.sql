insert into PUBLIC.COACH (CREATED_AT, ID, UPDATED_AT, CREATED_BY, EMAIL, NAME, PICTURE, UPDATED_BY)
values (null, UUID(), null, null, 'xiyuan.tyler@gmail.com', 'xiyuan tu',
        'https://lh3.googleusercontent.com/a/ACg8ocLJ3jmpfPUrzU7DtF_JfOXEaWuCD9PQRdwrMZ54SxUwV9xF3g=s96-c', null);

SET @coach_id = (SELECT ID
                 FROM PUBLIC.COACH
                 WHERE EMAIL = 'xiyuan.tyler@gmail.com');

insert into PUBLIC.STUDENT (CREATED_AT, ID, UPDATED_AT, CREATED_BY, EMAIL, NAME, PICTURE, UPDATED_BY, COACH, LOCATION)
values (null, UUID(), null, null, 'xiyuan.tuuu@gmail.com', 'Xiyuan',
        'https://lh3.googleusercontent.com/a/ACg8ocJ3GfDkAw4QTndY1VbtFu-Iuzjgv91Wv0jflSk6L5H16Ja6FQ=s96-c', null,
        @coach_id, 'MILPITAS');
