import { useState, useEffect } from 'react';
import {
    Container,
    Typography,
    Card,
    CardMedia,
    CardContent,
    CardActions,
    Button,
    Box,
    Chip,
    CircularProgress,
    Alert,
} from '@mui/material';
import { ShoppingCart, MusicNote } from '@mui/icons-material';
import { albumService, genreService, type Album, type Genre, type ApiError } from '../services';

const HomePage = () => {
    const [albums, setAlbums] = useState<Album[]>([]);
    const [genres, setGenres] = useState<Genre[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setLoading(true);
            setError(null);
            const [albumsData, genresData] = await Promise.all([
                albumService.getAll(),
                genreService.getAll(),
            ]);
            console.log('Albums data:', albumsData);
            console.log('Genres data:', genresData);
            setAlbums(Array.isArray(albumsData) ? albumsData.slice(0, 8) : []);
            setGenres(Array.isArray(genresData) ? genresData : []);
        } catch (err) {
            const apiError = err as ApiError;
            setError(apiError.message || 'Failed to load data');
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Container maxWidth="lg" sx={{ mt: 4 }}>
                <Alert severity="error" onClose={() => setError(null)}>
                    {error}
                </Alert>
            </Container>
        );
    }

    return (
        <Box>
            <Box
                sx={{
                    bgcolor: 'primary.main',
                    color: 'white',
                    py: 8,
                    mb: 6,
                }}
            >
                <Container maxWidth="lg">
                    <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
                        Welcome to Music Planet
                    </Typography>
                    <Typography variant="h5" sx={{ mb: 3 }}>
                        Discover amazing albums from your favorite artists
                    </Typography>
                    <Button
                        variant="contained"
                        color="secondary"
                        size="large"
                        startIcon={<MusicNote />}
                        href="/albums"
                    >
                        Browse Albums
                    </Button>
                </Container>
            </Box>

            <Container maxWidth="lg" sx={{ mb: 6 }}>
                <Typography variant="h4" gutterBottom fontWeight="bold">
                    Genres
                </Typography>
                <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap', mb: 4 }}>
                    {genres.map((genre) => (
                        <Chip
                            key={genre.id}
                            label={genre.name}
                            clickable
                            color="primary"
                            variant="outlined"
                        />
                    ))}
                </Box>

                <Typography variant="h4" gutterBottom fontWeight="bold">
                    Featured Albums
                </Typography>
                {albums.length === 0 ? (
                    <Typography variant="body1" color="text.secondary">
                        No albums available at the moment.
                    </Typography>
                ) : (
                    <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(3, 1fr)', lg: 'repeat(4, 1fr)' }, gap: 3 }}>
                        {albums.map((album) => (
                            <Card
                                key={album.id}
                                sx={{
                                    height: '100%',
                                    display: 'flex',
                                    flexDirection: 'column',
                                    transition: 'transform 0.2s',
                                    '&:hover': {
                                        transform: 'translateY(-4px)',
                                        boxShadow: 4,
                                    },
                                }}
                            >
                                <CardMedia
                                    component="img"
                                    height="200"
                                    image={album.coverImageUrl || 'https://via.placeholder.com/200'}
                                    alt={album.title}
                                    sx={{ objectFit: 'cover' }}
                                />
                                <CardContent sx={{ flexGrow: 1 }}>
                                    <Typography gutterBottom variant="h6" component="div" noWrap>
                                        {album.title}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary" noWrap>
                                        {album.artist?.name || 'Unknown Artist'}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        {album.genres && album.genres.length > 0 ? album.genres[0].name : 'Unknown'}
                                    </Typography>
                                    <Typography variant="h6" color="primary" sx={{ mt: 1 }}>
                                        ${album.price?.toFixed(2) || '0.00'}
                                    </Typography>
                                    <Typography variant="caption" color={album.stockQuantity > 0 ? 'success.main' : 'error.main'}>
                                        {album.stockQuantity > 0 ? `${album.stockQuantity} in stock` : 'Out of stock'}
                                    </Typography>
                                </CardContent>
                                <CardActions>
                                    <Button
                                        size="small"
                                        startIcon={<ShoppingCart />}
                                        variant="contained"
                                        fullWidth
                                        disabled={album.stockQuantity === 0}
                                    >
                                        Add to Cart
                                    </Button>
                                </CardActions>
                            </Card>
                        ))}
                    </Box>
                )}
            </Container>
        </Box>
    );
};

export default HomePage;
