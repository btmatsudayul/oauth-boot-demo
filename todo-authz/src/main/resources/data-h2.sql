INSERT INTO account(
    username, 
    password, 
    first_name, 
    last_name)
VALUES(
    'demo', 
    '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 
    'Taro', 
    'Yamada');

INSERT INTO oauth_client_details (
    client_id,
    client_secret,
    resource_ids, 
    scope,
    authorized_grant_types,
    web_server_redirect_uri,
    authorities,
    access_token_validity,
    refresh_token_validity,
    additional_information,
    autoapprove)
VALUES(
    'client',
    '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 
    'resourceA',
    'read,write,delete',
    'authorization_code,refresh_token',
    'http://localhost:8080/login',
    'ROLE_USER',
    '10',
    '86400',
    NULL,
    NULL
), (
    'resource-server',
    '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK', 
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL
);

COMMIT;