import React from 'react';
import './Navbar.css';

class Navbar extends React.PureComponent {

    // logoutHandler = (event) => {
    //     sessionStorage.clear();
    //     this.props.history.push("/");
    // }

    render() {
        return (
            <nav className="navbar">
                <h1 className="title">
                    <span className="logo">Upravljanje poslovnim procesima</span>
                </h1>

                <ul>
                    {sessionStorage.getItem('role') === 'ADMIN' ? <li><a href="/certificate">Certificate</a></li> : null}
                    {sessionStorage.getItem('token') === null ? <li><a href="/login">Login</a></li> : null}
                    {sessionStorage.getItem('token') === null ? <li><a href="/registration">Registration</a></li> : null}
                    {sessionStorage.getItem('token') !== null ? <li><a href="/login" onClick={(event) => this.logoutHandler(event)}>Logout</a></li> : null}
                </ul>
            </nav>
        );
    }
}

export default Navbar;