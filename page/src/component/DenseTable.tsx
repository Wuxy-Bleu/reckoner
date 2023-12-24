import * as React from 'react';
import {useState, useEffect} from 'react'
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TablePagination from '@mui/material/TablePagination';
import Paper from '@mui/material/Paper';
import {ThemeProvider, createTheme} from '@material-ui/core/styles';
import axios from 'axios';
import {reckoner} from "../types/reckoner";

function createData(
    name: string,
    calories: number,
    fat: number,
    carbs: number,
    protein: number,
) {
    return {name, calories, fat, carbs, protein};
}

const rows = [
    createData('Frozen yoghurt', 159, 6.0, 24, 4.0),
    createData('Ice cream sandwich', 237, 9.0, 37, 4.3),
    createData('Eclair', 262, 16.0, 24, 6.0),
    createData('Cupcake', 305, 3.7, 67, 4.3),
    createData('Gingerbread', 356, 16.0, 49, 3.9),
];

const headers = ['id', 'inOut', 'amount', 'currency', 'fromAcctName', 'toAcctName', 'transDate', 'createDate'];

const theme = createTheme();

export default function DenseTable() {


    const [data, setData] = useState<reckoner[]>([]);
    const [page, setPage] = React.useState(0);

    async function fetchData() {
        const result = await axios(
            'http://127.0.0.1:6722/reckoner',
        );
        const x = result.data as reckoner[];
        console.log(x)
        setData(x);
    }

    useEffect(() => {
        fetchData();
    }, []);

    const handleChangePage = (event: unknown, newPage: number) => {
        setPage(newPage);
    };

    return (
        <ThemeProvider theme={theme}>
            <TableContainer component={Paper} style={{textAlign: 'left'}}>
                <Table sx={{minWidth: 650}} size="small" aria-label="tableTitle">
                    <TableHead>
                        <TableRow>
                            {
                                headers.map((k) => (
                                    <TableCell key={k}>{k}</TableCell>
                                ))
                            }
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map( r => (
                            <TableRow
                                key={r.id}
                                sx={{'&:last-child td, &:last-child th': {border: 0}}}
                            >
                                <TableCell component="th" scope="row">
                                    {r.id}
                                </TableCell>
                                <TableCell>{r.inOut}</TableCell>
                                <TableCell>{r.amount}</TableCell>
                                <TableCell>{r.currency}</TableCell>
                                <TableCell>{r.fromAcctObj?.name}</TableCell>
                                <TableCell>{r.toAcctObj?.name}</TableCell>
                                <TableCell>{r.transDate}</TableCell>
                                <TableCell>{r.createdAt}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <TablePagination
                    // rowsPerPageOptions={[5, 10, 25]}
                    component="div"
                    count={-1}
                    rowsPerPage={5}
                    page={0}
                    onPageChange={handleChangePage}
                    // onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </TableContainer>
        </ThemeProvider>
    );
}
