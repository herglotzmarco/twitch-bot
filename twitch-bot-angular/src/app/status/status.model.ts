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

    static forName(status: string) {
        switch (status.toLowerCase()) {
            case 'stopped': return Status.stopped();
            case 'started': return Status.started();
            case 'starting': return Status.starting();
            case 'stopping': return Status.stopping();
            default:
                console.log('Unknown Status: ' + status);
                break;
        }
    }

}
