SET @coach_id = (SELECT ID
                 FROM PUBLIC.COACH
                 WHERE EMAIL = 'xiyuan.tyler@gmail.com');

// add 50 students
INSERT INTO PUBLIC.STUDENT (CREATED_AT, ID, UPDATED_AT, CREATED_BY, EMAIL, NAME, PICTURE, UPDATED_BY, DEFAULT_COACH,
                            LOCATION)
SELECT null,
       UUID(),
       null,
       null,
       CONCAT('test', n, '@gmail.com'),
       CONCAT('test', n),
       '',
       null,
       @coach_id,
       'MILPITAS'
FROM (SELECT ROW_NUMBER() OVER () AS n FROM SYSTEM_RANGE(1, 50)) numbers;

// add open horus for the next 10 days
INSERT INTO PUBLIC.OPEN_HOUR (CREATED_AT, END_AT, START_AT, UPDATED_AT, COACH, ID, CREATED_BY, UPDATED_BY)
SELECT null,
       PARSEDATETIME(DATEADD('DAY', n, CURRENT_DATE) || ' 23:00:00', 'yyyy-MM-dd HH:mm:ss'),
       PARSEDATETIME(DATEADD('DAY', n, CURRENT_DATE) || ' 00:00:00', 'yyyy-MM-dd HH:mm:ss'),
       null,
       @coach_id,
       UUID(),
       null,
       null
FROM (SELECT ROW_NUMBER() OVER () AS n FROM SYSTEM_RANGE(0, 9)) numbers;

insert into SLOT (CREATED_AT, END_AT, START_AT, UPDATED_AT, COACH, ID, STUDENT, CREATED_BY, UPDATED_BY, STATUS,
                  CLASS_ID, COACH_DELETED, STUDENT_DELETED)
SELECT null,
       TIMESTAMPADD(HOUR, (ROW_NUMBER() OVER (ORDER BY s.id) - 1) + 8,
                    DATEADD('DAY', 1, CURRENT_DATE) + TIME '00:00:00'),
       TIMESTAMPADD(HOUR, (ROW_NUMBER() OVER (ORDER BY s.id) - 1), DATEADD('DAY', 1, CURRENT_DATE) + TIME '00:00:00'),
       null,
       @coach_id,
       UUID(),
       s.id,
       null,
       null,
       'AVAILABLE',
       UUID(),
       false,
       false
FROM student s;