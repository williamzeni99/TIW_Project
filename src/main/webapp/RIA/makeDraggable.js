

function makeDraggable(elements){

    for (let i = elements.length - 1; i >= 0; i--) {
        doDraggable(elements[i]);
    }

    let startE;

    /**Set all element graphically draggable*/
    function doDraggable(element){
        element.draggable = true;
        element.addEventListener("dragstart", dragStart); //save dragged element reference
        element.addEventListener("dragover", dragOver); // change color of reference element to red
        element.addEventListener("dragleave", dragLeave); // change color of reference element to black
        element.addEventListener("drop", drop); //change position of dragged element using the referenced element
    }

    /**
     * The dragstart event is fired when the user starts
     * dragging an element (if it is draggable=True).
     * */
    function dragStart(event){
        startE=event.target.closest("li")
    }

    /**
     * The dragover event is fired when an element
     * is being dragged over a valid drop target.
     * */
    function dragOver(event){
        event.preventDefault();

        let dest= event.target.closest("li");
        if(isInsideID(startE.getAttribute("id"), dest.getAttribute("id"))) return;
        dest.className="selected";
    }

    /**
     * The dragleave event is fired when a dragged element leaves
     * a valid drop target.
     * */
    function dragLeave(event){
        let dest = event.target.closest("li");

        // Mark  the current element as "notselected", then with CSS we will put it in black
        dest.className = "notselected";
    }


    /**
     * The drop event is fired when an element or text selection
     * is dropped on a valid drop target.
     * */
    function drop(event){
        event.stopImmediatePropagation();
        document.getElementById("errorTopicMsg").textContent="";
        let dest = event.target.closest("li");
        let start_id=startE.getAttribute("id");
        let dest_id=dest.getAttribute("id");
        if(isInsideID(start_id,dest_id)) return;
        let txt= "Do you wanna move topic "+ start_id + " into topic "+ dest_id+ "?";

        if(confirm(txt)){
            reset(dest);
            let x= updateTree(startE, dest);
            if(x===-1){
                document.getElementById("errorTopicMsg").textContent="You cannot move this topic here. Size limit reached.";
            }
            if(x===-2){
                document.getElementById("errorTopicMsg").textContent="Something graphical went wrong. Unexpected error occurred. ";

            }
            RESET(false);
        }
        else{
            reset(dest);
        }

    }

    function reset(element){
        element.className="notselected";
    }


}