import { AppBar, Toolbar, Typography, Button, Badge, IconButton, Box } from '@mui/material';
import { ShoppingCart, Home, Album, Person } from '@mui/icons-material';
import { Link as RouterLink } from 'react-router-dom';

const Header = () => {
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

                <IconButton
                    color="inherit"
                    component={RouterLink}
                    to="/cart"
                >
                    <Badge badgeContent={0} color="secondary">
                        <ShoppingCart />
                    </Badge>
                </IconButton>
            </Toolbar>
        </AppBar>
    );
};

export default Header;
