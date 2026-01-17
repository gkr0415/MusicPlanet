import { AppBar, Toolbar, Typography, Button, Badge, IconButton, Box } from '@mui/material';
import { ShoppingCart, Home, Album, Person, Login, Logout } from '@mui/icons-material';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Header = () => {
    const navigate = useNavigate();
    const { isAuthenticated, user, logout } = useAuth();

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" component="div" sx={{ flexGrow: 0, mr: 4 }}>
                    Music Planet
                </Typography>

                <Box sx={{ flexGrow: 1, display: 'flex', gap: 2 }}>
                    <Button
                        color="inherit"
                        component={RouterLink}
                        to="/"
                        startIcon={<Home />}
                    >
                        Home
                    </Button>
                    <Button
                        color="inherit"
                        component={RouterLink}
                        to="/albums"
                        startIcon={<Album />}
                    >
                        Albums
                    </Button>
                    <Button
                        color="inherit"
                        component={RouterLink}
                        to="/artists"
                        startIcon={<Person />}
                    >
                        Artists
                    </Button>
                </Box>

                {isAuthenticated && user && (
                    <Typography variant="body2" sx={{ mr: 2 }}>
                        Welcome, {user.firstName}
                    </Typography>
                )}

                <IconButton
                    color="inherit"
                    component={RouterLink}
                    to="/cart"
                    sx={{ mr: 1 }}
                >
                    <Badge badgeContent={0} color="secondary">
                        <ShoppingCart />
                    </Badge>
                </IconButton>

                {isAuthenticated ? (
                    <Button
                        color="inherit"
                        onClick={handleLogout}
                        startIcon={<Logout />}
                    >
                        Logout
                    </Button>
                ) : (
                    <Button
                        color="inherit"
                        component={RouterLink}
                        to="/login"
                        startIcon={<Login />}
                    >
                        Login
                    </Button>
                )}
            </Toolbar>
        </AppBar>
    );
};

export default Header;
