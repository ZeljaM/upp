export const BASE_URL = 'http://localhost:8081';

export const LOGIN_URL = `${BASE_URL}/api/auth/sign-in`;

// Registration reader url
export const REGISTRATION_URL =  `${BASE_URL}/register/user`
export const REGISTRATION_START_URL = `${REGISTRATION_URL}/process`
export const REGISTRATION_NEXT_URL = `${REGISTRATION_URL}/task`

// Registration writer url
export const REGISTRATION_WRITER_URL =  `${BASE_URL}/register/writer`
export const REGISTRATION_WRITER_START_URL = `${REGISTRATION_WRITER_URL}/process`
export const REGISTRATION_WRITER_NEXT_URL = `${REGISTRATION_WRITER_URL}/task`
export const REGISTRATION_WRITER_UPLOAD_FILES_URL = `${REGISTRATION_WRITER_URL}/files`


// Tasks
export const TASK_URL =  `${BASE_URL}/api/task`
export const GET_TASKS_OF_USER =  `${TASK_URL}/`
export const FORM_FOR_TASK = `${TASK_URL}/form`



