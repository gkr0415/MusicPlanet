import { Container, Typography } from '@mui/material';

const CartPage = () => {
    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Shopping Cart
            </Typography>
            <Typography variant="body1">
                Your cart is empty
            </Typography>
        </Container>
    );
};

export default CartPage;
