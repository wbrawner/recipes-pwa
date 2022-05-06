import {html} from "../lit-core.min.js";
import {StatefulElement} from "./stateful.js";
import {LoginAction} from "../actions/authentication.js";

export class LoginForm extends StatefulElement {
    static get properties() {
        return {
            registration: {type: Boolean},
            loading: {type: Boolean},
            error: {type: String},
            username: {type: String},
            email: {type: String},
            password: {type: String},
            confirmPassword: {type: String}
        }
    }

    constructor() {
        super();
        this.username = '';
        this.email = '';
        this.password = '';
        this.confirmPassword = '';
    }

    #login(e) {
        e.preventDefault()
        this.dispatch(new LoginAction(this.username, this.password))
    }

    #toggleRegistration() {
        this.registration = !this.registration;
    }

    #email() {
        if (!this.registration) {
            return null;
        }
        return html`<input type="email" @change="${(e) => this.email = e.target.value}" placeholder="Email" required
                           .value="${this.email}"/>`
    }

    #confirmPassword() {
        if (!this.registration) {
            return null;
        }
        return html`<input type="password" @change="${(e) => this.confirmPassword = e.target.value}"
                           placeholder="Confirm Password" required .value="${this.confirmPassword}"/>`
    }

    #toggleButton() {
        let message = "Need to create an account?"
        let action = "Register"
        if (this.registration) {
            message = "Already have an account?"
            action = "Login"
        }
        return html`<span>${message}</span>
        <button @click="${this.#toggleRegistration}" class="link">${action}</button>`
    }

    render() {
        if (this.state.loading) {
            return html`
                <animated-loader></animated-loader>`
        }
        const error = this.error ? html`<p class="error">${this.error}</p>` : null
        return html`
            <form @submit="${this.#login}">
                ${error}
                <input type="text" @change="${(e) => this.username = e.target.value}" placeholder="Username" required
                       .value="${this.username}"/>
                ${this.#email()}
                <input type="password" @change="${(e) => this.password = e.target.value}" placeholder="Password"
                       required .value="${this.password}"/>
                ${this.#confirmPassword()}
                <input type="submit" .value="${this.registration ? 'Register' : 'Login'}"/>
            </form>
            ${this.#toggleButton()}
        `
    }
}

customElements.define('login-form', LoginForm)