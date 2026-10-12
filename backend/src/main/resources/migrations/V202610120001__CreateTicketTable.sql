CREATE TABLE IF NOT EXISTS ticket (
    id              BIGINT          NOT NULL    AUTO_INCREMENT,
    title           VARCHAR(200)    NOT NULL,
    description     VARCHAR(2000)   NULL,
    status          VARCHAR(20)     NOT NULL    DEFAULT 'OPEN',
    priority        VARCHAR(20)     NOT NULL    DEFAULT 'MEDIUM',
    created_at      TIMESTAMP(6)    NOT NULL    DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      TIMESTAMP(6)    NOT NULL    DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT ticket_pk PRIMARY KEY (id)
);

CREATE INDEX ticket_status_idx ON ticket (status);
