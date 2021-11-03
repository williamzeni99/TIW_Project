

function makeDraggable(){
    var elements = document.getElementsByClassName("draggable")
    for (let i = elements.length - 1; i >= 0; i--) {
        elements[i].draggable = true;
        elements[i].addEventListener("dragstart", dragStart); //save dragged element reference
        elements[i].addEventListener("dragover", dragOver); // change color of reference element to red
        elements[i].addEventListener("dragleave", dragLeave); // change color of reference element to black
        elements[i].addEventListener("drop", drop); //change position of dragged element using the referenced element
    }

    let startE;
    /*
    The dragstart event is fired when the user starts
    dragging an element (if it is draggable=True)
    */
    function dragStart(event){
        startE=event.target.closest("li")
    }

    /*
        The dragover event is fired when an element
        is being dragged over a valid drop target.
     */
    function dragOver(event){
        event.preventDefault();

        var dest= event.target.closest("li");
        dest.className="selected";
    }

    /*
        The dragleave event is fired when a dragged
        element leaves a valid drop target.
    */
    function dragLeave(event){
        var dest = event.target.closest("li");

        // Mark  the current element as "notselected", then with CSS we will put it in black
        dest.className = "notselected";
    }

    /*
        The drop event is fired when an element or text selection is dropped on a valid drop target.
    */
    function drop(event){
        event.stopImmediatePropagation();
        var dest = event.target.closest("li");
        var txt= "Do you wanna move topic "+ startE.getAttribute("id") + " into topic "+ dest.getAttribute("id")+ "?";

        if(confirm(txt)){
            //updateTree(startE.getAttribute("id"), dest.getAttribute("id"));
        }
        else{
            reset(dest);
        }

    }

    function reset(dest){
        dest.className="notselected";
    }


}