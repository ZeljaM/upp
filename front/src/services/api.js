const headers = {
  Accept: '*/*',
  'Content-Type': 'application/json;charset=utf-8',
};

const headers01 = {
  Accept: '*/*',
  'Content-Type': 'multipart/form-data',
};

/** `helper` method for resolving headers */
const resolveHeaders = (authToken) =>
  authToken
    ? { ...headers, Authorization: `Bearer ${authToken}` }
    : { ...headers };

/** `helper` method for resolving headers */
const resolveHeaders01 = (authToken) =>
  authToken
    ? { Authorization: `Bearer ${authToken}` }
    : { ...headers01 };

// Used for post, put, patch, get data
const Get = (url, authToken = '', options = {}) =>
  fetch(url, {
    method: 'GET',
    headers: resolveHeaders(authToken),
    ...options,
  });

const Post = (url, data = {}, authToken = '', options = {}, multipart = false) => {
  const resolve = multipart ? resolveHeaders01 : resolveHeaders;
  return fetch(url, {
    method: 'POST',
    headers: resolve(authToken),
    body: JSON.stringify(data),
    ...options,
  });
};

const PostFile = (url, data = {}, authToken = '') => {
  return fetch(url, {
    method: 'POST',
    headers: resolveHeaders01(authToken),
    body: data,
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

export { Get, Post, Patch, Put, PostFile };
