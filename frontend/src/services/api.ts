import axios, { AxiosError } from 'axios';

export interface ApiError {
    message: string;
    status?: number;
    errors?: Record<string, string[]>;
}

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api',
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 10000,
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('auth_token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        if (import.meta.env.DEV) {
            console.log(`[API Request] ${config.method?.toUpperCase()} ${config.url}`);
        }

        return config;
    },
    (error) => {
        console.error('[API Request Error]', error);
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => {
        if (import.meta.env.DEV) {
            console.log(`[API Response] ${response.config.method?.toUpperCase()} ${response.config.url}`, response.status);
        }
        return response;
    },
    (error: AxiosError<ApiError>) => {
        const apiError: ApiError = {
            message: 'An unexpected error occurred',
            status: error.response?.status,
        };

        if (error.response) {
            switch (error.response.status) {
                case 400:
                    apiError.message = error.response.data?.message || 'Bad request. Please check your input.';
                    apiError.errors = error.response.data?.errors;
                    break;
                case 401:
                    apiError.message = 'Unauthorized. Please log in.';
                    localStorage.removeItem('auth_token');
                    break;
                case 403:
                    apiError.message = 'Forbidden. You do not have permission to perform this action.';
                    break;
                case 404:
                    apiError.message = error.response.data?.message || 'Resource not found.';
                    break;
                case 409:
                    apiError.message = error.response.data?.message || 'Conflict. Resource already exists.';
                    break;
                case 500:
                    apiError.message = 'Internal server error. Please try again later.';
                    break;
                default:
                    apiError.message = error.response.data?.message || `Server error: ${error.response.status}`;
            }
        } else if (error.request) {
            apiError.message = 'No response from server. Please check your internet connection.';
        } else {
            apiError.message = error.message || 'Failed to send request.';
        }

        console.error('[API Error]', apiError);
        return Promise.reject(apiError);
    }
);

export default api;
