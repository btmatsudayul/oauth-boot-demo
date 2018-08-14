
DROP TABLE IF EXISTS account;
CREATE TABLE account(
    username VARCHAR(128),
    password VARCHAR(60),
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    CONSTRAINT account_pk PRIMARY KEY (username)
);

DROP TABLE IF EXISTS oauth_client_details;
CREATE TABLE oauth_client_details (
    client_id VARCHAR(255) NOT NULL,
    client_secret VARCHAR(255) DEFAULT NULL,
    resource_ids VARCHAR(255) DEFAULT NULL,
    scope VARCHAR(255) DEFAULT NULL,
    authorized_grant_types VARCHAR(255) DEFAULT NULL,
    web_server_redirect_uri VARCHAR(255) DEFAULT NULL,
    authorities VARCHAR(255) DEFAULT NULL,
    access_token_validity int(11) DEFAULT NULL,
    refresh_token_validity int(11) DEFAULT NULL,
    additional_information VARCHAR(4096) DEFAULT NULL,
    autoapprove VARCHAR(255) DEFAULT NULL,
    CONSTRAINT oauth_client_details_pk PRIMARY KEY (client_id)
);

DROP TABLE IF EXISTS oauth_code;
CREATE TABLE oauth_code (
    code VARCHAR(255) NOT NULL,
    authentication BLOB NOT NULL,
    CONSTRAINT oauth_code_pk PRIMARY KEY (code)
);

DROP TABLE IF EXISTS oauth_approvals;
CREATE TABLE oauth_approvals (
    userId VARCHAR(255) NOT NULL,
    clientId VARCHAR(255) NOT NULL,
    scope VARCHAR(255) DEFAULT NULL,
    status VARCHAR(8) DEFAULT NULL,
    expiresAt TIMESTAMP DEFAULT NULL,
    lastModifiedAt TIMESTAMP DEFAULT NULL,
    CONSTRAINT oauth_approvals_pk PRIMARY KEY (userId, clientId, scope)
);

