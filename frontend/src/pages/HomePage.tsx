import { Container, Typography } from '@mui/material';

const HomePage = () => {
    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Typography variant="h3" component="h1" gutterBottom>
                Welcome to Music Planet
            </Typography>
            <Typography variant="body1">
                Your one-stop shop for all your music needs!
            </Typography>
        </Container>
    );
};

export default HomePage;
