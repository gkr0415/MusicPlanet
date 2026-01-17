import api from './api';

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    customer: {
        id: number;
        firstName: string;
        lastName: string;
        email: string;
    };
}

class AuthService {
    private readonly endpoint = '/auth';

    async login(credentials: LoginRequest): Promise<AuthResponse> {
        const response = await api.post<AuthResponse>(`${this.endpoint}/login`, credentials);
        if (response.data.token) {
            localStorage.setItem('auth_token', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data.customer));
        }
        return response.data;
    }

    async register(userData: RegisterRequest): Promise<AuthResponse> {
        const response = await api.post<AuthResponse>(`${this.endpoint}/register`, userData);
        if (response.data.token) {
            localStorage.setItem('auth_token', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data.customer));
        }
        return response.data;
    }

    logout(): void {
        localStorage.removeItem('auth_token');
        localStorage.removeItem('user');
    }

    getToken(): string | null {
        return localStorage.getItem('auth_token');
    }

    getCurrentUser() {
        const userStr = localStorage.getItem('user');
        if (!userStr) return null;

        try {
            return JSON.parse(userStr);
        } catch (error) {
            console.error('Failed to parse user data from localStorage:', error);
            localStorage.removeItem('user');
            return null;
        }
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }
}

export default new AuthService();
