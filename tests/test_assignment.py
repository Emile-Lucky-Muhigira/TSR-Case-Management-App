"""Black-box checks against the assignment. Run: python -m unittest discover -s tests -v"""
from pathlib import Path
import subprocess
import tempfile
import unittest

ROOT = Path(__file__).resolve().parents[1]


class AssignmentChecks(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.build = tempfile.TemporaryDirectory()
        subprocess.run(['javac', '-d', cls.build.name,
                        *map(str, (ROOT / 'src').rglob('*.java'))], check=True)

    @classmethod
    def tearDownClass(cls):
        cls.build.cleanup()

    def setUp(self):
        self.folder = tempfile.TemporaryDirectory()
        self.addCleanup(self.folder.cleanup)

    def run_app(self, text):
        result = subprocess.run(['java', '-cp', self.build.name, 'Main'],
                                input=text, text=True, capture_output=True,
                                cwd=self.folder.name, timeout=10)
        self.assertEqual(result.returncode, 0, result.stderr)
        self.assertEqual(result.stderr, '')
        return result.stdout

    def add(self, title='Login', kind='Manual', detail='Tester'):
        return f'1\n{title}\nDescription keyword\nSigned out\nDashboard visible\n{kind}\n{detail}\n'

    def test_missing_file_empty_view_and_zero_report(self):
        output = self.run_app('2\n5\n6\n')
        self.assertIn('No test cases found.', output)
        self.assertIn('Total Test Cases : 0', output)
        self.assertIn('0.0%', output)

    def test_required_fields_rejected(self):
        output = self.run_app('1\n   \n1\nTitle\nDescription\nNone\n   \n5\n6\n')
        self.assertIn('Title is required.', output)
        self.assertIn('Expected result is required.', output)
        self.assertIn('Total Test Cases : 0', output)

    def test_invalid_menu_type_id_and_status(self):
        output = self.run_app('bad\n' + self.add(kind='bad\nmanual') +
                              '3\nabc\n3\n999999999999999\n3\n999\n3\n1\nbad\n1\n6\n')
        for expected in ['Invalid choice.', 'Please enter Manual or Automated.',
                         'Invalid ID.', 'No test case found with ID 999.',
                         'Invalid status choice.', 'Status updated to PASS.']:
            self.assertIn(expected, output)

    def test_search_title_description_empty_and_no_match(self):
        self.run_app(self.add() + self.add('Other', 'Automated', 'scripts/test.java') + '6\n')
        for query in ['LOGIN', 'KEYWORD', '']:
            output = self.run_app(f'4\n{query}\n6\n')
            self.assertIn('Login', output)
            if query != 'LOGIN':
                self.assertIn('Other', output)
            else:
                self.assertNotIn('Other', output)
        self.assertIn('No test cases found.', self.run_app('4\nno-match\n6\n'))

    def test_all_statuses_and_report_rounding(self):
        self.run_app(self.add() + self.add('Second') + self.add('Third', 'Automated') + '6\n')
        for choice, status in [('1', 'PASS'), ('2', 'FAIL'), ('3', 'BLOCKED'), ('4', 'NOT_RUN')]:
            output = self.run_app(f'3\n1\n{choice}\n2\n6\n')
            self.assertIn(f'Status updated to {status}.', output)
        output = self.run_app('3\n1\n1\n3\n2\n2\n3\n3\n3\n5\n6\n')
        for expected in ['Total Test Cases : 3', 'Manual      : 2', 'Automated   : 1',
                         'PASS            : 1', 'FAIL            : 1', 'BLOCKED         : 1',
                         'NOT RUN         : 0', '33.3%']:
            self.assertIn(expected, output)

    def test_all_fields_roundtrip_and_unique_ids(self):
        self.run_app(self.add() + self.add('Auto', 'Automated', 'scripts/test.java') + '3\n1\n1\n6\n')
        path = Path(self.folder.name) / 'testcases.properties'
        before = path.read_text(encoding='utf-8')
        self.assertIn('test.0.status=PASS', before)
        self.assertIn('test.1.status=NOT_RUN', before)
        self.assertIn('test.1.specificDetails=scripts/test.java', before)
        self.run_app('6\n')
        after = path.read_text(encoding='utf-8')
        records = lambda text: sorted(line for line in text.splitlines() if not line.startswith('#'))
        self.assertEqual(records(before), records(after))
        self.run_app(self.add('Third') + '6\n')
        self.assertIn('test.2.id=3', path.read_text(encoding='utf-8'))

    def test_previous_version_file_loads(self):
        path = Path(self.folder.name) / 'testcases.properties'
        path.write_text('count=1\ntest.0.id=7\ntest.0.type=Manual\ntest.0.title=Legacy\n'
                        'test.0.description=Original\ntest.0.preconditions=None\n'
                        'test.0.expectedResult=Success\ntest.0.status=BLOCKED\n'
                        'test.0.specificDetails=Original tester\n', encoding='utf-8')
        output = self.run_app('2\n' + self.add('New') + '6\n')
        self.assertIn('Legacy', output)
        self.assertIn('BLOCKED', output)
        self.assertIn('Original tester', output)
        self.assertIn('test.1.id=8', path.read_text(encoding='utf-8'))

    def test_corrupt_file_is_not_overwritten(self):
        path = Path(self.folder.name) / 'testcases.properties'
        path.write_text('count=invalid', encoding='utf-8')
        self.assertIn('Could not load saved tests:', self.run_app(''))
        self.assertEqual(path.read_text(), 'count=invalid')

    def test_save_failure_keeps_app_running(self):
        (Path(self.folder.name) / 'testcases.properties.tmp').mkdir()
        output = self.run_app('6\n2\n')
        self.assertIn('Could not save tests:', output)
        self.assertIn('No test cases found.', output)

    def test_closed_input_does_not_crash(self):
        for text in ['', '1\n', '3\n']:
            self.assertIn('Input closed.', self.run_app(text))


if __name__ == '__main__':
    unittest.main()
