const headers = {
  Accept: '*/*',
  'Content-Type': 'application/json;charset=utf-8',
};

/** `helper` method for resolving headers */
const resolveHeaders = (authToken) =>
  authToken
    ? { ...headers, Authorization: `Bearer ${authToken}` }
    : { ...headers };

// Used for post, put, patch, get data
const Get = (url, authToken = '', options = {}) =>
  fetch(url, {
    method: 'GET',
    headers: resolveHeaders(authToken),
    ...options,
  });

const Post = (url, data = {}, authToken = '', options = {}) => {
  return fetch(url, {
    method: 'POST',
    headers: resolveHeaders(authToken),
    body: JSON.stringify(data),
    ...options,
  });
};

const Patch = (url, data = {}, authToken = '', options = {}) => {
  return fetch(url, {
    method: 'PATCH',
    headers: resolveHeaders(authToken),
    body: JSON.stringify(data),
    ...options,
  });
};

const Put = (url, data = {}, authToken = '', options = {}) => {
  return fetch(url, {
    method: 'PUT',
    headers: resolveHeaders(authToken),
    body: JSON.stringify(data),
    ...options,
  });
};

export { Get, Post, Patch, Put };
