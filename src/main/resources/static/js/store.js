import {StatefulElement} from "./components/stateful.js"; // needed for the routes to work
import {User} from "./model/user.js";
import {
    Action, ACTION_LOGIN, ACTION_LOGOUT, ACTION_REGISTER, ACTION_VIEW_LOGIN, ACTION_VIEW_RECIPES, ACTION_VIEW_REGISTER,
} from "./actions/authentication.js";
import {routes} from "./components/router.js";
import {apiService} from "./api-service.js";

export class State {
    /**
     * @type Route
     */
    route;

    /**
     * @type User
     */
    user;

    /**
     * @type boolean
     */
    loading = false;

    /**
     * @type string
     */
    error;

    /**
     * @type Array<Recipe>
     */
    recipes;
}

class Store {

    /**
     * @type State
     */
    #state = new State();

    /**
     * @type Array<StatefulMixin(LitElement)>
     */
    #subscribers = [];

    constructor() {
        const sessionToken = window.localStorage.getItem('session')
        if (!sessionToken) {
            this.dispatch(new Action(ACTION_VIEW_LOGIN))
            return
        }
        apiService.sessionToken = sessionToken
        this.#updateState({
            loading: true
        })
        apiService.getProfile().then(user => {
            this.#updateState({
                user: user,
            })
            this.dispatch(new Action(ACTION_VIEW_RECIPES))
        })
        console.log('starting location', window.location.href)
    }

    /**
     * @param {Action} action
     */
    dispatch(action) {
        switch (action.name) {
            case ACTION_VIEW_RECIPES:
                this.#updateState({
                    route: routes['/'],
                    loading: true
                })
                this.#getRecipes()
                break;
            case ACTION_VIEW_LOGIN:
                this.#updateState({
                    ...this.#state,
                    route: routes['/login']
                })
                break;
            case ACTION_VIEW_REGISTER:
                this.#updateState({
                    ...this.#state,
                    route: routes['/register']
                })
                break;
            case ACTION_LOGIN:
                this.#login(action)
                break;
            case ACTION_LOGOUT:
                window.localStorage.removeItem('session')
                this.#updateState(new State())
                this.dispatch(new Action(ACTION_VIEW_LOGIN))
                break;
            case ACTION_REGISTER:
                this.#register(action)
                break;
            default:
                console.warn("Unhandled action!", action)
        }
    }

    /**
     * @param {LoginAction} action
     */
    #login(action) {
        this.#updateState({
            loading: true,
            error: undefined,
        })
        apiService.login(action.username, action.password)
            .then(async (session) => {
                window.localStorage.setItem('session', session.token)
                apiService.sessionToken = session.token
                const user = await apiService.getProfile()
                this.#updateState({
                    user: user,
                })
                this.dispatch(new Action(ACTION_VIEW_RECIPES))
            })
            .catch(e => {
                console.error('login error', e)
                this.#updateState({
                    loading: false,
                    error: e
                })
            })
    }

    /**
     *
     * @param {RegisterAction} action
     */
    #register(action) {
        if (action.password !== action.confirmPassword) {
            this.#updateState({
                error: 'Passwords don\'t match'
            })
            return
        }
        this.#updateState({
            loading: true,
            error: undefined
        })
    }

    #getRecipes() {
        apiService.getRecipes()
            .then(recipes => {
                this.#updateState({
                    recipes: recipes,
                    loading: false
                })
            })
            .catch(e => {
                console.error('error fetching recipes', e)
                this.#updateState({
                    loading: false,
                    error: e
                })
            })
    }

    #updateState(updates) {
        this.#state = {...this.#state, ...updates}
        this.#subscribers.forEach(subscriber => {
            console.log('updating state for subscriber', subscriber)
            subscriber.onStateChange({...this.#state})
            subscriber.requestUpdate()
        })
    }

    currentState() {
        return {...this.#state};
    }

    /**
     * Subscribe to updates to the state
     * @param {StatefulMixin(LitElement)} subscriber
     */
    subscribe(subscriber) {
        this.#subscribers = [...this.#subscribers, subscriber]
        subscriber.onStateChange(this.#state)
        subscriber.requestUpdate()
    }

    /**
     * Unsubscribe from state updates
     * @param {StatefulMixin(LitElement)} subscriber
     */
    unsubscribe(subscriber) {
        this.#subscribers = this.#subscribers.filter(s => s !== subscriber)
    }
}

export const store = new Store()