import {Session} from "../model/session.js"
import {User} from "../model/user.js"

export class Action {
    constructor(name) {
        this.name = name;
    }
}
export const ACTION_VIEW_RECIPES = "view-recipes";
export const ACTION_VIEW_LOGIN = "view-login";
export const ACTION_VIEW_REGISTER = "view-register";
export const ACTION_LOGIN = 'login'
export const ACTION_LOGOUT = 'logout'
export const ACTION_REGISTER = 'register'

export class LoginAction extends Action {
    constructor(username, password) {
        super(ACTION_LOGIN);
        this.username = username;
        this.password = password;
    }
}

export class RegisterAction extends Action {
    constructor(username, email, password, confirmPassword) {
        super(ACTION_REGISTER);
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
