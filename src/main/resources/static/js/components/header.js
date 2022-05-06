import {css, html} from "../lit-core.min.js";
import {StatefulElement} from "./stateful.js";
import {Action, ACTION_LOGOUT} from "../actions/authentication.js";

export class Header extends StatefulElement {
    static get styles() {
        return css`
            header {
                background: orange;
                padding: 1em;
            }
            
            h1 {
                display: inline-block;
                margin: 0;
            } `
    }

    #logout = (e) => this.dispatch(new Action(ACTION_LOGOUT))

    #logoutButton() {
        if (!this.state.user) {
            return null
        }
        return html`<button class="link" @click="${this.#logout}">Logout</button> `
    }

    render() {
        return html`
            <header>
                <h1>${this.state?.route?.title || 'Recipes'}</h1>
                ${this.#logoutButton()}
            </header>`
    }
}

customElements.define('app-header', Header)