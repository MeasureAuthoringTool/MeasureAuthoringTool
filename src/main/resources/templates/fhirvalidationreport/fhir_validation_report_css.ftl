<style>
    body {
        margin: auto;
        font-family: Arial Unicode MS, Arial, sans-serif;
    }

    .report-container {
        padding: 0% 5%;
        margin-right: auto;
        margin-left: auto;
    }

    .report-header {
        padding: 16px;
        background-color: #dcdcdc;
        font-size: 1.6em;
    }

    .report-body {
        margin: 24px 0px;
    }

    .custom-card {
        background-color: #DCE9F5;
        padding: 16px;
        margin-bottom: 16px;
    }

    .card-header {
        font-size: 1.15em;
        font-weight: bold;
        margin-bottom: 8px;
    }

    .card-body {
        background-color: white;
    }

    .card-no-errors{
        padding: 0px;
        background-color: transparent;
    }

    .card-body .container{
        padding: 12px 8px;
    }

    .card-no-errors .container{
        padding: 32px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
        color: lightblue;
    }

    .color-sky-blue{
        color: #2E86C1;;
    }

    label {
        font-size: 15px;
    }

    .value {
        padding-top: 2px;
    }

    .font-smaller {
        font-size: smaller;
    }

    .table {
        width: 100%;
        max-width: 100%;
        margin-bottom: 1rem;
        border-collapse: collapse;
        font-family: Arial Unicode MS, Arial, sans-serif;
        font-size: small;
        border: 1px solid #000;
        background-color: #fff;
    }

    .table-striped>tbody>tr:nth-of-type(odd) {
        background-color: #f9f9f9
    }

    .table th, .table td {
        padding: 0.75rem;
        vertical-align: top;
        text-align: left;
        border-left: 1px solid;
    }

    .table thead th {
        vertical-align: bottom;
        background-color: lightgrey;
        white-space: nowrap;
        border-bottom: 1px solid;
    }

    .table tbody + tbody, .table td {
        border-top: 1px solid #eceeef;
    }

    .table-hover tbody tr:hover {
        background-color: rgba(0, 0, 0, 0.075);
    }

    .table-hover .table-active:hover {
        background-color: rgba(0, 0, 0, 0.075);
    }

    .table-hover .table-active:hover > td,
    .table-hover .table-active:hover > th {
        background-color: rgba(0, 0, 0, 0.075);
    }

    .error-msg {
        margin-top: 1%;
    }
</style>
