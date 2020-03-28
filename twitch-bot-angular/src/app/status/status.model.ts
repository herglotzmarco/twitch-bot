export class Status {

    private constructor(public name: string) { }

    static stopped() {
        return new Status('stopped');
    }

    static started() {
        return new Status('started');
    }

    static starting() {
        return new Status('starting');
    }

    static stopping() {
        return new Status('stopping');
    }

}
