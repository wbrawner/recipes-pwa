import {html} from '../lit-core.min.js'
import {StatefulElement} from "./stateful.js";
import {Header} from "./header.js";
import {SplashScreen} from "./splash.js";
import {Router} from "./router.js";

export class RecipeApp extends StatefulElement {
    render() {
        console.log('recipe-app state', this.state)
        return html`
            <splash-screen .visible="${this.state?.route === undefined}"></splash-screen>
            <app-header></app-header>
            <app-router></app-router>
        `
    }
}

customElements.define('recipe-app', RecipeApp)