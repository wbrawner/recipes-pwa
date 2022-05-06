import {html} from "../lit-core.min.js";
import {StatefulElement} from "./stateful.js";
import {store} from "../store.js";

export class RegisterForm extends StatefulElement {
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

    #toggleRegistration() {
        this.registration = !this.registration;
    }

    async #authenticate(e) {
        e.preventDefault()
        this.error = null;
        this.loading = true;
        if (this.registration && this.password !== this.confirmPassword) {
            this.error = 'Passwords must match'
            this.loading = false
            return
        }
        const endpoint = this.registration ? 'register' : 'login'
        let body = {
            username: this.username,
            password: this.password
        }
        if (this.registration) {
            body = {
                ...body,
                email: this.email
            }
        }
        try {
            const response = await fetch(`/api/users/${endpoint}`, {
                method: 'POST',
                body: JSON.stringify(body),
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            if (response.status === 401) {
                console.error('401', await response.json())
                this.error = 'Invalid credentials'
                return
            }
            if (!response.ok) {
                const errorResponse = await response.json()
                this.error = errorResponse.message
                console.error('Request failed', errorResponse)
                return
            }
            const session = await response.json()
            console.log('session', session)
            await this.#getProfile(session)
        } catch (e) {
            console.error(e)
            this.error = e;
        } finally {
            this.loading = false;
        }
    }

    async #getProfile(session) {
        const response = await fetch('/api/users/me', {
            headers: {
                'Authorization': `Bearer ${session.token}`
            }
        })
        if (response.status === 401) {
            this.error = 'Invalid credentials'
            return
        }
        if (!response.ok) {
            const errorResponse = await response.json()
            this.error = errorResponse.message
            console.error('Request failed', errorResponse)
            return
        }
        const user = await response.json()
        document.dispatchEvent(new AuthenticationEvent(session, user))
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
        if (this.loading) {
            return html`
                <animated-loader></animated-loader>`
        }
        const error = this.error ? html`<p class="error">${this.error}</p>` : null
        return html`
            <form @submit="${this.#authenticate}">
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

customElements.define('register-form', RegisterForm)