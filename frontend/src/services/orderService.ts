import api from './api';
import type { Album } from './albumService';
import type { Customer } from './customerService';

export interface OrderItem {
    id: number;
    album: Album;
    quantity: number;
    price: number;
}

export interface Order {
    id: number;
    customer: Customer;
    orderItems: OrderItem[];
    totalPrice: number;
    status: string;
    orderDate: string;
}

export interface CreateOrderItemRequest {
    albumId: number;
    quantity: number;
}

export interface CreateOrderRequest {
    customerId: number;
    orderItems: CreateOrderItemRequest[];
}

export interface UpdateOrderRequest {
    status?: string;
}

class OrderService {
    private readonly endpoint = '/orders';

    async getAll(): Promise<Order[]> {
        const response = await api.get<Order[]>(this.endpoint);
        return response.data;
    }

    async getById(id: number): Promise<Order> {
        const response = await api.get<Order>(`${this.endpoint}/${id}`);
        return response.data;
    }

    async getByCustomer(customerId: number): Promise<Order[]> {
        const response = await api.get<Order[]>(`${this.endpoint}/customer/${customerId}`);
        return response.data;
    }

    async create(orderData: CreateOrderRequest): Promise<Order> {
        const response = await api.post<Order>(this.endpoint, orderData);
        return response.data;
    }

    async update(id: number, orderData: UpdateOrderRequest): Promise<Order> {
        const response = await api.put<Order>(`${this.endpoint}/${id}`, orderData);
        return response.data;
    }

    async delete(id: number): Promise<void> {
        await api.delete(`${this.endpoint}/${id}`);
    }
}

export default new OrderService();
