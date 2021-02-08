import * as helper from "./helpers";
import * as gridRowActions from "./MAT/GridRowActions";
import * as cqlLibrary from "../../elements/CqlLibraryElements";

Cypress.Commands.add("deleteCqlLibrary", (cqlLibraryName) => {
    helper.visibleWithTimeout('#CQLLibrarySearchCellTable')
    cy.get('#CQLLibrarySearchCellTable > :nth-child(3)').each(element => {
        if (cy.wrap(element).contains(cqlLibraryName)) {
            gridRowActions.selectRow(element)
        } else {
            cy.log(cqlLibraryName + ' is not available')
        }
    })
    cy.get(cqlLibrary.deleteCqllibrariesBtn).click()

    cy.get(cqlLibrary.confirmDeleteText).type('DELETE', { force: true })
    cy.get(cqlLibrary.confirmDeleteBtn).click()
})
