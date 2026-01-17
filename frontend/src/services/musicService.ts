import api from './api';

export interface Artist {
    id: number;
    name: string;
    bio?: string;
    imageUrl?: string;
}

export interface Genre {
    id: number;
    name: string;
    description?: string;
}

export interface Album {
    id: number;
    title: string;
    artist: Artist;
    genre: Genre;
    releaseDate: string;
    price: number;
    stockQuantity: number;
    imageUrl?: string;
}

export interface Song {
    id: number;
    title: string;
    album: Album;
    duration: number;
    trackNumber: number;
}

export interface Customer {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phone?: string;
    address?: string;
}

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

// Artist API
export const artistService = {
    getAll: () => api.get<Artist[]>('/artists'),
    getById: (id: number) => api.get<Artist>(`/artists/${id}`),
    create: (artist: Partial<Artist>) => api.post<Artist>('/artists', artist),
    update: (id: number, artist: Partial<Artist>) => api.put<Artist>(`/artists/${id}`, artist),
    delete: (id: number) => api.delete(`/artists/${id}`),
};

// Album API
export const albumService = {
    getAll: () => api.get<Album[]>('/albums'),
    getById: (id: number) => api.get<Album>(`/albums/${id}`),
    create: (album: Partial<Album>) => api.post<Album>('/albums', album),
    update: (id: number, album: Partial<Album>) => api.put<Album>(`/albums/${id}`, album),
    delete: (id: number) => api.delete(`/albums/${id}`),
};

// Genre API
export const genreService = {
    getAll: () => api.get<Genre[]>('/genres'),
    getById: (id: number) => api.get<Genre>(`/genres/${id}`),
    create: (genre: Partial<Genre>) => api.post<Genre>('/genres', genre),
    update: (id: number, genre: Partial<Genre>) => api.put<Genre>(`/genres/${id}`, genre),
    delete: (id: number) => api.delete(`/genres/${id}`),
};

// Song API
export const songService = {
    getAll: () => api.get<Song[]>('/songs'),
    getById: (id: number) => api.get<Song>(`/songs/${id}`),
    getByAlbum: (albumId: number) => api.get<Song[]>(`/songs/album/${albumId}`),
    create: (song: Partial<Song>) => api.post<Song>('/songs', song),
    update: (id: number, song: Partial<Song>) => api.put<Song>(`/songs/${id}`, song),
    delete: (id: number) => api.delete(`/songs/${id}`),
};

// Customer API
export const customerService = {
    getAll: () => api.get<Customer[]>('/customers'),
    getById: (id: number) => api.get<Customer>(`/customers/${id}`),
    create: (customer: Partial<Customer>) => api.post<Customer>('/customers', customer),
    update: (id: number, customer: Partial<Customer>) => api.put<Customer>(`/customers/${id}`, customer),
    delete: (id: number) => api.delete(`/customers/${id}`),
};

// Order API
export const orderService = {
    getAll: () => api.get<Order[]>('/orders'),
    getById: (id: number) => api.get<Order>(`/orders/${id}`),
    create: (order: Partial<Order>) => api.post<Order>('/orders', order),
    update: (id: number, order: Partial<Order>) => api.put<Order>(`/orders/${id}`, order),
    delete: (id: number) => api.delete(`/orders/${id}`),
};
