import {css, html, LitElement} from "../lit-core.min.js";

export class SplashScreen extends LitElement {
    static get properties() {
        return {
            visible: {type: Boolean},
            hide: {type: Boolean}
        }
    }

    static get styles() {
        return css`
            .splash-bg {
                width: 100vw;
                height: 100vh;
                box-sizing: border-box;
                background: orange;
                padding: 1em;
                display: flex;
                justify-content: center;
                align-items: center;
                position: absolute;
                top: 0;
                left: 0;
                opacity: 1;
                transition: all 0.25s ease;
            }
            
            .fadeOut {
                opacity: 0;
            }
        `
    }

    render() {
        if (this.hide) {
            return null
        }
        if (!this.visible) {
            setTimeout(() => {
                this.hide = true
            }, 250)
        }
        return html`
            <div class="splash-bg ${this.visible ? '' : 'fadeOut'}">
                <!-- TODO: Get an icon to show here -->
                <h1>Recipes</h1>
            </div>
        `
    }
}

customElements.define('splash-screen', SplashScreen)