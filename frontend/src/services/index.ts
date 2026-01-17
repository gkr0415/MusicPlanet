export { default as artistService } from './artistService';
export { default as albumService } from './albumService';
export { default as genreService } from './genreService';
export { default as customerService } from './customerService';
export { default as orderService } from './orderService';

export type { Artist, CreateArtistRequest, UpdateArtistRequest } from './artistService';
export type { Album, CreateAlbumRequest, UpdateAlbumRequest } from './albumService';
export type { Genre, CreateGenreRequest, UpdateGenreRequest } from './genreService';
export type { Customer, CreateCustomerRequest, UpdateCustomerRequest } from './customerService';
export type { Order, OrderItem, CreateOrderRequest, CreateOrderItemRequest, UpdateOrderRequest } from './orderService';
export type { ApiError } from './api';
