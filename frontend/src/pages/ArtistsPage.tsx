import { Container, Typography } from '@mui/material';

const ArtistsPage = () => {
    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Artists
            </Typography>
            <Typography variant="body1">
                Discover amazing artists
            </Typography>
        </Container>
    );
};

export default ArtistsPage;
