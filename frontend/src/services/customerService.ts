import api from './api';

export interface Customer {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phone?: string;
    address?: string;
}

export interface CreateCustomerRequest {
    firstName: string;
    lastName: string;
    email: string;
    phone?: string;
    address?: string;
}

export interface UpdateCustomerRequest {
    firstName?: string;
    lastName?: string;
    email?: string;
    phone?: string;
    address?: string;
}

class CustomerService {
    private readonly endpoint = '/customers';

    async getAll(): Promise<Customer[]> {
        const response = await api.get<Customer[]>(this.endpoint);
        return response.data;
    }

    async getById(id: number): Promise<Customer> {
        const response = await api.get<Customer>(`${this.endpoint}/${id}`);
        return response.data;
    }

    async getByEmail(email: string): Promise<Customer> {
        const response = await api.get<Customer>(`${this.endpoint}/email/${email}`);
        return response.data;
    }

    async create(customerData: CreateCustomerRequest): Promise<Customer> {
        const response = await api.post<Customer>(this.endpoint, customerData);
        return response.data;
    }

    async update(id: number, customerData: UpdateCustomerRequest): Promise<Customer> {
        const response = await api.put<Customer>(`${this.endpoint}/${id}`, customerData);
        return response.data;
    }

    async delete(id: number): Promise<void> {
        await api.delete(`${this.endpoint}/${id}`);
    }
}

export default new CustomerService();
