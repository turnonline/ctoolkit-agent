var comps = angular.module( 'components', [] );

comps.controller( "ChangesetController", function ()
{
    this.item = {name: "John", surname: "foo"};
    this.item2 = {name: "Foo", surname: "Bar"};
    this.item3 = {name: "Bar", surname: "Loo"};

    this.displayedItems = [this.item, this.item2, this.item3];

    this.addItem = function ()
    {
        this.displayedItems.push( this.item );
    };

    this.removeItem = function(item) {
        this.displayedItems.splice(this.displayedItems.indexOf(item), 1);
    };
} );

comps.directive( 'formRow', function ()
{
    return {
        restrict: 'E',
        scope: {label: '@', value: '@'},
        template: '<div>' +
        '<label>{{label}}:</label>' +
        '<strong>{{value}}</strong>' +
        '</div>',
        replace: true
    };
} );
