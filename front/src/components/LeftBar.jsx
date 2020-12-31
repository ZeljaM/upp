import React from 'react';
import './LeftBar.css';

class LeftBar extends React.PureComponent {

    render() {
        return(
            <div className="menu-wrap">
                <input type="checkbox" className="toggler" />
                <div className="hamburger">
                    <div></div>
                </div>
                <div className="menu">
                    <div>
                        <div>
                            <ul>
                              {localStorage.getItem('access_token') === null ? <li><a href="/login">Login</a></li> : null}
                              {localStorage.getItem('access_token') === null ? <li><a href="/registration">Registration</a></li> : null}
                              {localStorage.getItem('access_token') !== null ? <li><a href="/login" onClick={() => localStorage.clear()}>Logout</a></li> : null}
                              <li><a href="/tasks">Tasks</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default LeftBar;