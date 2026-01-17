import { Container, Typography } from '@mui/material';

const AlbumsPage = () => {
    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Albums
            </Typography>
            <Typography variant="body1">
                Browse our collection of albums
            </Typography>
        </Container>
    );
};

export default AlbumsPage;
