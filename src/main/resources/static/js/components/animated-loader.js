import {css, html, LitElement} from "../lit-core.min.js";

export class AnimatedLoader extends LitElement {
    static get styles() {
        return css`
        `
    }

    render() {
        return html`
            <p>Loading...</p>
        `
    }
}

customElements.define('animated-loader', AnimatedLoader)