import api from './api';

export interface Artist {
    id: number;
    name: string;
    bio?: string;
    imageUrl?: string;
}

export interface CreateArtistRequest {
    name: string;
    bio?: string;
    imageUrl?: string;
}

export interface UpdateArtistRequest {
    name?: string;
    bio?: string;
    imageUrl?: string;
}

class ArtistService {
    private readonly endpoint = '/artists';

    async getAll(): Promise<Artist[]> {
        const response = await api.get<Artist[]>(this.endpoint);
        return response.data;
    }

    async getById(id: number): Promise<Artist> {
        const response = await api.get<Artist>(`${this.endpoint}/${id}`);
        return response.data;
    }

    async create(artistData: CreateArtistRequest): Promise<Artist> {
        const response = await api.post<Artist>(this.endpoint, artistData);
        return response.data;
    }

    async update(id: number, artistData: UpdateArtistRequest): Promise<Artist> {
        const response = await api.put<Artist>(`${this.endpoint}/${id}`, artistData);
        return response.data;
    }

    async delete(id: number): Promise<void> {
        await api.delete(`${this.endpoint}/${id}`);
    }
}

export default new ArtistService();
