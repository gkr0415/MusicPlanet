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
    genres: Genre[];
    releaseDate: string;
    price: number;
    stockQuantity: number;
    coverImageUrl?: string;
    description?: string;
    format?: string;
    label?: string;
}

export interface CreateAlbumRequest {
    title: string;
    artistId: number;
    genreId: number;
    releaseDate: string;
    price: number;
    stockQuantity: number;
    imageUrl?: string;
}

export interface UpdateAlbumRequest {
    title?: string;
    artistId?: number;
    genreId?: number;
    releaseDate?: string;
    price?: number;
    stockQuantity?: number;
    imageUrl?: string;
}

class AlbumService {
    private readonly endpoint = '/albums';

    async getAll(): Promise<Album[]> {
        const response = await api.get<any>(this.endpoint);
        return response.data.content || response.data;
    }

    async getById(id: number): Promise<Album> {
        const response = await api.get<Album>(`${this.endpoint}/${id}`);
        return response.data;
    }

    async getByArtist(artistId: number): Promise<Album[]> {
        const response = await api.get<Album[]>(`${this.endpoint}/artist/${artistId}`);
        return response.data;
    }

    async getByGenre(genreId: number): Promise<Album[]> {
        const response = await api.get<Album[]>(`${this.endpoint}/genre/${genreId}`);
        return response.data;
    }

    async create(albumData: CreateAlbumRequest): Promise<Album> {
        const response = await api.post<Album>(this.endpoint, albumData);
        return response.data;
    }

    async update(id: number, albumData: UpdateAlbumRequest): Promise<Album> {
        const response = await api.put<Album>(`${this.endpoint}/${id}`, albumData);
        return response.data;
    }

    async delete(id: number): Promise<void> {
        await api.delete(`${this.endpoint}/${id}`);
    }
}

export default new AlbumService();
