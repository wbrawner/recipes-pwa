import {LitElement} from "../lit-core.min.js";
import {State, store} from "../store.js";

export const StatefulMixin = (superClass) => class extends superClass{

    constructor() {
        super();
        this.state = store.currentState()
    }

    connectedCallback() {
        super.connectedCallback();
        console.log('connected', this)
        store.subscribe(this)
    }

    disconnectedCallback() {
        console.log('disconnected', this)
        store.unsubscribe(this)
        super.disconnectedCallback();
    }

    /**
     * @param {Action} action
     */
    dispatch(action) {
        store.dispatch(action)
    }

    onStateChange(state) {
        this.state = state;
    }
}

export const StatefulElement = StatefulMixin(LitElement)