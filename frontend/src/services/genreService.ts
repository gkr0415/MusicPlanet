import api from './api';

export interface Genre {
    id: number;
    name: string;
    description?: string;
}

export interface CreateGenreRequest {
    name: string;
    description?: string;
}

export interface UpdateGenreRequest {
    name?: string;
    description?: string;
}

class GenreService {
    private readonly endpoint = '/genres';

    async getAll(): Promise<Genre[]> {
        const response = await api.get<Genre[]>(this.endpoint);
        return response.data;
    }

    async getById(id: number): Promise<Genre> {
        const response = await api.get<Genre>(`${this.endpoint}/${id}`);
        return response.data;
    }

    async create(genreData: CreateGenreRequest): Promise<Genre> {
        const response = await api.post<Genre>(this.endpoint, genreData);
        return response.data;
    }

    async update(id: number, genreData: UpdateGenreRequest): Promise<Genre> {
        const response = await api.put<Genre>(`${this.endpoint}/${id}`, genreData);
        return response.data;
    }

    async delete(id: number): Promise<void> {
        await api.delete(`${this.endpoint}/${id}`);
    }
}

export default new GenreService();
