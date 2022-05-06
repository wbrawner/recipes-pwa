import {StatefulElement} from "./stateful.js";
import {LoginForm} from "./login.js";
import {RecipeList} from "./recipe-list.js";
import {RegisterForm} from "./register.js";
import {html, LitElement} from "../lit-core.min.js";

class Route {
    constructor(title, component, path) {
        this.title = title;
        this.component = component;
        this.path = path;
    }
}

export const routes = {
    '/': new Route(null, html`<recipe-list></recipe-list>`, '/'),
    '/login': new Route('Login', html`<login-form></login-form>`, '/login'),
    '/register': new Route('Register', html`<register-form></register-form>`, '/register'),
};

export class Router extends StatefulElement {
    onStateChange(state) {
        super.onStateChange(state);
        let title = 'Recipes'
        if (state.route?.title) {
            title += ' - ' + state.route.title
        }
        document.title = title;
        window.history.pushState(null, null, state.route?.path)
    }

    render() {
        return this.state.route?.component
    }
}

customElements.define('app-router', Router)