import {
    artistService,
    albumService,
    genreService,
    customerService,
    orderService,
    type Artist,
    type Album,
    type CreateOrderRequest,
    type ApiError,
} from './services';

export const fetchAllArtists = async (): Promise<Artist[]> => {
    try {
        const artists = await artistService.getAll();
        console.log('Artists:', artists);
        return artists;
    } catch (error) {
        const apiError = error as ApiError;
        console.error('Error fetching artists:', apiError.message);
        throw error;
    }
};

export const createNewArtist = async () => {
    try {
        const newArtist = await artistService.create({
            name: 'The Beatles',
            bio: 'Legendary British rock band',
            imageUrl: 'https://example.com/beatles.jpg',
        });
        console.log('Created artist:', newArtist);
        return newArtist;
    } catch (error) {
        const apiError = error as ApiError;
        console.error('Error creating artist:', apiError.message);
        if (apiError.errors) {
            console.error('Validation errors:', apiError.errors);
        }
        throw error;
    }
};

export const fetchAlbumsByArtist = async (artistId: number): Promise<Album[]> => {
    try {
        const albums = await albumService.getByArtist(artistId);
        console.log(`Albums by artist ${artistId}:`, albums);
        return albums;
    } catch (error) {
        const apiError = error as ApiError;
        console.error('Error fetching albums:', apiError.message);
        throw error;
    }
};

export const createNewOrder = async () => {
    try {
        const orderData: CreateOrderRequest = {
            customerId: 1,
            orderItems: [
                { albumId: 1, quantity: 2 },
                { albumId: 3, quantity: 1 },
            ],
        };

        const order = await orderService.create(orderData);
        console.log('Created order:', order);
        return order;
    } catch (error) {
        const apiError = error as ApiError;
        console.error('Error creating order:', apiError.message);
        throw error;
    }
};

export const updateAlbumStock = async (albumId: number, newStock: number) => {
    try {
        const updatedAlbum = await albumService.update(albumId, {
            stockQuantity: newStock,
        });
        console.log('Updated album:', updatedAlbum);
        return updatedAlbum;
    } catch (error) {
        const apiError = error as ApiError;
        console.error('Error updating album:', apiError.message);
        throw error;
    }
};

export const deleteGenre = async (genreId: number) => {
    try {
        await genreService.delete(genreId);
        console.log(`Genre ${genreId} deleted successfully`);
    } catch (error) {
        const apiError = error as ApiError;
        console.error('Error deleting genre:', apiError.message);
        throw error;
    }
};

export const findCustomerByEmail = async (email: string) => {
    try {
        const customer = await customerService.getByEmail(email);
        console.log('Customer found:', customer);
        return customer;
    } catch (error) {
        const apiError = error as ApiError;
        if (apiError.status === 404) {
            console.log('Customer not found with email:', email);
        } else {
            console.error('Error finding customer:', apiError.message);
        }
        throw error;
    }
};
