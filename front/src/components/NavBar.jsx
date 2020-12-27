import React from 'react';
import './Navbar.css';

class Navbar extends React.PureComponent {
    render() {
        return (
            <nav className="navbar">
                <h1 className="title">
                    <span className="logo">Upp</span>
                </h1>
                <ul>
                    {localStorage.getItem('access_token') === null ? <li><a href="/login">Login</a></li> : null}
                    <li><a href="/registration">Registration</a></li>
                    {localStorage.getItem('access_token') !== null ? <li><a href="/login" onClick={() => localStorage.clear()}>Logout</a></li> : null}
                </ul>
            </nav>
        );
    }
}

export default Navbar;